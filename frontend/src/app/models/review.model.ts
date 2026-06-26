export interface ReviewRequest {
  comment: string;
  rating: number;
  bookId: number;
}

export interface ReviewResponse {
  id: number;
  userName: string;
  comment: string;
  rating: number;
  bookTitle: string;
  authorFullName: string;
  bookId: number;
}
