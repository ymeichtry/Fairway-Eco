import { create } from "zustand";
import { persist } from "zustand/middleware";
import { GolfBall } from "@/types/golfball";
import { CartItem } from "@/types/cart";

interface CartStore {
  items: CartItem[];
  isOpen: boolean;
  addItem: (golfBall: GolfBall, quantity?: number) => void;
  removeItem: (golfBallId: number) => void;
  updateQuantity: (golfBallId: number, quantity: number) => void;
  clearCart: () => void;
  toggleCart: () => void;
  openCart: () => void;
  closeCart: () => void;
  totalItems: () => number;
  totalAmount: () => number;
}

export const useCartStore = create<CartStore>()(
  persist(
    (set, get) => ({
      items: [],
      isOpen: false,

      addItem: (golfBall, quantity = 1) => {
        set((state) => {
          const existingItem = state.items.find(
            (item) => item.golfBall.id === golfBall.id
          );

          if (existingItem) {
            return {
              items: state.items.map((item) =>
                item.golfBall.id === golfBall.id
                  ? { ...item, quantity: item.quantity + quantity }
                  : item
              ),
              isOpen: true,
            };
          }

          return { 
            items: [...state.items, { golfBall, quantity }],
            isOpen: true,
          };
        });
      },

      removeItem: (golfBallId) => {
        set((state) => ({
          items: state.items.filter((item) => item.golfBall.id !== golfBallId),
        }));
      },

      updateQuantity: (golfBallId, quantity) => {
        if (quantity <= 0) {
          get().removeItem(golfBallId);
          return;
        }
        set((state) => ({
          items: state.items.map((item) =>
            item.golfBall.id === golfBallId ? { ...item, quantity } : item
          ),
        }));
      },

      clearCart: () => set({ items: [] }),

      toggleCart: () => set((state) => ({ isOpen: !state.isOpen })),
      openCart: () => set({ isOpen: true }),
      closeCart: () => set({ isOpen: false }),

      totalItems: () =>
        get().items.reduce((sum, item) => sum + item.quantity, 0),

      totalAmount: () =>
        get().items.reduce(
          (sum, item) => sum + item.golfBall.price * item.quantity,
          0
        ),
    }),
    { 
      name: "fairwayeco-cart",
      partialize: (state) => ({ items: state.items }),
    }
  )
);
