export interface ChatRequest {
  message: string;
  conversationId?: string;
}

export interface ChatResponse {
  success: boolean;
  summary: string;
  books: BookItem[];
  chartData: ChartData[];
  userEmail: string;
}

export interface BookItem {
  title: string;
  author: string;
  genre: string;
  price: string;
  stock: number;
}

export interface ChartData {
  label: string;
  value: number;
}

// Keep old names as aliases for backward compatibility during migration
export type AiChatRequest = ChatRequest;
export type AgentChatResponse = ChatResponse;
