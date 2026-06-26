import { Component, signal, ViewChild, ElementRef, AfterViewChecked, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule, FormBuilder } from '@angular/forms';
import { ChatService } from '../../../../core/services/chat.service';
import { AuthService } from '../../../../core/services/auth.service';
import { ChatResponse, BookItem, ChartData } from '../../../../models/ai.models';

interface ChatMessage {
  role: 'user' | 'assistant';
  content: string;
  timestamp: Date;
  books?: BookItem[];
  chartData?: ChartData[];
}

@Component({
  selector: 'app-chat-page',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './chat-page.component.html',
  styleUrl: './chat-page.component.css'
})
export class ChatPageComponent implements OnInit, AfterViewChecked {
  @ViewChild('messagesContainer') private messagesContainer!: ElementRef;

  private fb = inject(FormBuilder);
  private chatService = inject(ChatService);
  private auth = inject(AuthService);

  messages = signal<ChatMessage[]>([]);
  loading = signal(false);

  chatForm = this.fb.group({
    message: ['']
  });

  // Solo preguntas que el agente puede responder con sus herramientas
  exampleSuggestions = [
    'Busca libros de terror',
    'Tienes libros de ciencia ficcion?',
    'Recomiendame libros segun mis compras',
    'Busca libros sobre amor y aventura',
    'Dame un reporte de ventas del ultimo mes',
    'Cuales son los libros mas vendidos?'
  ];

  private shouldScroll = false;

  ngOnInit(): void {
    const key = this.storageKey();
    const saved = localStorage.getItem(key);
    if (saved) {
      this.messages.set(JSON.parse(saved));
      this.shouldScroll = true;
    }
  }

  ngAfterViewChecked(): void {
    if (this.shouldScroll) {
      this.scrollToBottom();
      this.shouldScroll = false;
    }
  }

  sendMessage(content?: string): void {
    const text = content ?? this.chatForm.get('message')?.value?.trim() ?? '';
    if (!text || this.loading()) return;

    const userMsg: ChatMessage = { role: 'user', content: text, timestamp: new Date() };
    this.addMessage(userMsg);

    this.chatForm.get('message')?.setValue('');
    this.loading.set(true);
    this.shouldScroll = true;

    const convId = this.auth.email();
    this.chatService.chat(text, convId).subscribe({
      next: (response: ChatResponse) => {
        let summary = response.summary || '';
        if (summary.trim().startsWith('{') || summary.trim().startsWith('[')) {
          try {
            const parsed = JSON.parse(summary);
            summary = parsed.summary || parsed.message || 'Respuesta procesada correctamente.';
          } catch {
            summary = 'Respuesta procesada correctamente.';
          }
        }
        const assistantMsg: ChatMessage = {
          role: 'assistant',
          content: summary,
          timestamp: new Date(),
          books: response.books?.length ? response.books : undefined,
          chartData: response.chartData?.length ? response.chartData : undefined
        };
        this.addMessage(assistantMsg);
        this.loading.set(false);
        this.shouldScroll = true;
      },
      error: () => {
        const errorMsg: ChatMessage = {
          role: 'assistant',
          content: 'Lo siento, hubo un error al procesar tu mensaje. Intenta de nuevo.',
          timestamp: new Date()
        };
        this.addMessage(errorMsg);
        this.loading.set(false);
        this.shouldScroll = true;
      }
    });
  }

  onKeydown(event: KeyboardEvent): void {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
      this.sendMessage();
    }
  }

  formatTime(date: Date): string {
    return new Date(date).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
  }

  getMaxChartValue(chartData: ChartData[]): number {
    return Math.max(...chartData.map(d => d.value), 1);
  }

  private addMessage(msg: ChatMessage): void {
    this.messages.update(list => [...list, msg]);
    localStorage.setItem(this.storageKey(), JSON.stringify(this.messages()));
  }

  private storageKey(): string {
    return `chat_messages_${this.auth.email()}`;
  }

  private scrollToBottom(): void {
    try {
      const el = this.messagesContainer?.nativeElement;
      if (el) el.scrollTop = el.scrollHeight;
    } catch (_) {}
  }
}
