import { GolfBall } from "./golfball";

export interface CartItem {
  golfBall: GolfBall;
  quantity: number;
}

export interface Cart {
  items: CartItem[];
  totalItems: number;
  totalAmount: number;
}
