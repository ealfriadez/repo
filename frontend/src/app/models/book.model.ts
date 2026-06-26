export interface BookRequest {
  title: string;
  genre: string;
  price: number;
  stock: number;
  minStock?: number;
  authorId: number;
  description: string;
  slug?: string;
}

export interface BookResponse {
  id: number;
  title: string;
  slug: string;
  genre: string;
  price: number;
  stock: number;
  description: string;
  authorFullName: string;
  imageUrl: string | null;
  hasFile: boolean;
}

export interface BulkUploadResponse {
  created: number;
  updated: number;
  errors: BulkError[];
}

export interface BulkError {
  row: number;
  message: string;
}
