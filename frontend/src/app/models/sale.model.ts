export interface SaleItemRequest {
  bookId: number;
  quantity: number;
}

export interface SaleRequest {
  items: SaleItemRequest[];
}

export interface SaleItemResponse {
  id: number;
  bookTitle: string;
  quantity: number;
  unitPrice: number;
  subtotal: number;
}

export interface SaleResponse {
  id: number;
  customerId: number;
  customerName: string;
  total: number;
  createdAt: string;
  items: SaleItemResponse[];
}
