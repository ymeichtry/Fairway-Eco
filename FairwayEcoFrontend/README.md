# Fairway-Eco Frontend

## Projektübersicht

Next.js Frontend für den Fairway-Eco Golf Ball Shop - ein Online-Shop für recycelte Golfbälle aus Wasserhindernissen.

## Technologie-Stack

- **Next.js 14+** (App Router)
- **React 18+**
- **TypeScript**
- **Tailwind CSS**
- **shadcn/ui** (UI Components)
- **Zustand** oder **React Query** (State Management)
- **Axios** oder **Fetch** (API Calls)

## Zu implementierende Features

### 🛒 Shop Features

- [ ] Produktliste mit Filtern (Marke, Zustand, Preis)
- [ ] Produktdetailseite
- [ ] Warenkorb (Cart)
- [ ] Checkout-Prozess
- [ ] Bestellbestätigung

### 👤 Kunden Features

- [ ] Registrierung
- [ ] Login
- [ ] Kundenprofil
- [ ] Bestellhistorie
- [ ] Adressverwaltung

### 🔧 Admin Features (Optional)

- [ ] Produkte verwalten (CRUD)
- [ ] Bestellungen verwalten
- [ ] Kunden verwalten
- [ ] Dashboard mit Statistiken

## Backend API

Das Backend läuft auf `http://localhost:8080` mit folgenden Endpoints:

### Golf Balls API

```typescript
// GET alle Golfbälle
GET /api/v1/golf-balls

// GET verfügbare Golfbälle (Lagerbestand > 0)
GET /api/v1/golf-balls/available

// GET mit Filtern
GET /api/v1/golf-balls/filter?brand=Titleist&condition=GRADE_A&minPrice=1.00&maxPrice=5.00

// GET alle Marken
GET /api/v1/golf-balls/brands

// GET nach Marke
GET /api/v1/golf-balls/brand/{brand}

// GET nach Zustand
GET /api/v1/golf-balls/condition/{condition}

// GET einzelner Golfball
GET /api/v1/golf-balls/{id}
```

### Customers API

```typescript
// POST neuer Kunde (Registrierung)
POST /api/v1/customers
Body: {
  "firstName": "Max",
  "lastName": "Mustermann",
  "email": "max@example.com",
  "phone": "+41 79 123 45 67",
  "address": {
    "street": "Golfstrasse 1",
    "city": "Zürich",
    "postalCode": "8000",
    "country": "Schweiz"
  }
}

// GET Kunde nach ID
GET /api/v1/customers/{id}

// GET Kunde nach Email
GET /api/v1/customers/email/{email}

// PUT Kunde aktualisieren
PUT /api/v1/customers/{id}
```

### Orders API

```typescript
// POST neue Bestellung
POST /api/v1/orders
Body: {
  "customerId": 1,
  "items": [
    { "golfBallId": 1, "quantity": 10 },
    { "golfBallId": 2, "quantity": 5 }
  ],
  "shippingAddress": {
    "street": "Lieferstrasse 5",
    "city": "Bern",
    "postalCode": "3000",
    "country": "Schweiz"
  }
}

// GET Bestellungen eines Kunden
GET /api/v1/orders/customer/{customerId}

// GET Bestellung nach ID
GET /api/v1/orders/{id}

// POST Bestellung stornieren
POST /api/v1/orders/{id}/cancel
```

## Datenmodelle (TypeScript Types)

```typescript
// types/golfball.ts
export type BallCondition =
  | "MINT"
  | "GRADE_A"
  | "GRADE_B"
  | "GRADE_C"
  | "PRACTICE";

export interface GolfBall {
  id: number;
  brand: string;
  model: string;
  price: number;
  quantity: number;
  condition: BallCondition;
  conditionDescription: string;
  description?: string;
  imageUrl?: string;
  createdAt: string;
  updatedAt: string;
}

// types/customer.ts
export interface Address {
  street: string;
  city: string;
  postalCode: string;
  country: string;
}

export interface Customer {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phone?: string;
  address?: Address;
  createdAt: string;
  updatedAt: string;
}

// types/order.ts
export type OrderStatus =
  | "PENDING"
  | "PAID"
  | "PROCESSING"
  | "SHIPPED"
  | "DELIVERED"
  | "CANCELLED"
  | "REFUNDED";

export interface OrderItem {
  id: number;
  golfBallId: number;
  golfBallBrand: string;
  golfBallModel: string;
  quantity: number;
  unitPrice: number;
  subtotal: number;
}

export interface Order {
  id: number;
  customerId: number;
  customerName: string;
  items: OrderItem[];
  status: OrderStatus;
  statusDescription: string;
  totalAmount: number;
  shippingAddress: Address;
  createdAt: string;
  updatedAt: string;
}

// types/cart.ts (Frontend only)
export interface CartItem {
  golfBall: GolfBall;
  quantity: number;
}

export interface Cart {
  items: CartItem[];
  totalItems: number;
  totalAmount: number;
}
```

## Empfohlene Projektstruktur

```
FairwayEcoFrontend/
├── app/
│   ├── layout.tsx
│   ├── page.tsx                    # Homepage
│   ├── products/
│   │   ├── page.tsx                # Produktliste
│   │   └── [id]/
│   │       └── page.tsx            # Produktdetail
│   ├── cart/
│   │   └── page.tsx                # Warenkorb
│   ├── checkout/
│   │   └── page.tsx                # Checkout
│   ├── orders/
│   │   ├── page.tsx                # Bestellhistorie
│   │   └── [id]/
│   │       └── page.tsx            # Bestelldetail
│   ├── account/
│   │   ├── page.tsx                # Profil
│   │   ├── login/
│   │   │   └── page.tsx
│   │   └── register/
│   │       └── page.tsx
│   └── admin/                      # (Optional)
│       ├── page.tsx                # Dashboard
│       ├── products/
│       ├── orders/
│       └── customers/
├── components/
│   ├── ui/                         # shadcn/ui components
│   ├── layout/
│   │   ├── Header.tsx
│   │   ├── Footer.tsx
│   │   └── Navbar.tsx
│   ├── products/
│   │   ├── ProductCard.tsx
│   │   ├── ProductGrid.tsx
│   │   ├── ProductFilter.tsx
│   │   └── ConditionBadge.tsx
│   ├── cart/
│   │   ├── CartItem.tsx
│   │   ├── CartSummary.tsx
│   │   └── CartIcon.tsx
│   └── orders/
│       ├── OrderCard.tsx
│       └── OrderStatusBadge.tsx
├── lib/
│   ├── api/
│   │   ├── client.ts               # Axios/Fetch Setup
│   │   ├── golfballs.ts            # Golf Ball API calls
│   │   ├── customers.ts            # Customer API calls
│   │   └── orders.ts               # Order API calls
│   ├── utils.ts                    # Utility functions
│   └── constants.ts                # App constants
├── hooks/
│   ├── useCart.ts                  # Cart state management
│   ├── useProducts.ts              # Product fetching
│   └── useAuth.ts                  # Authentication
├── store/                          # Zustand stores (if using Zustand)
│   ├── cartStore.ts
│   └── authStore.ts
├── types/
│   ├── golfball.ts
│   ├── customer.ts
│   ├── order.ts
│   └── cart.ts
├── public/
│   └── images/
├── .env.local
├── next.config.js
├── tailwind.config.js
├── tsconfig.json
└── package.json
```

## Setup-Befehle

```bash
# Projekt erstellen
npx create-next-app@latest FairwayEcoFrontend --typescript --tailwind --eslint --app --src-dir=false

# In Projektordner wechseln
cd FairwayEcoFrontend

# shadcn/ui installieren
npx shadcn@latest init

# Wichtige shadcn Komponenten installieren
npx shadcn@latest add button card input label select badge sheet dialog table form toast

# Zusätzliche Dependencies
npm install zustand axios lucide-react

# Entwicklungsserver starten
npm run dev
```

## Umgebungsvariablen

```env
# .env.local
NEXT_PUBLIC_API_URL=http://localhost:8080/api/v1
```

## API Client Beispiel

```typescript
// lib/api/client.ts
import axios from "axios";

const apiClient = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

export default apiClient;

// lib/api/golfballs.ts
import apiClient from "./client";
import { GolfBall, BallCondition } from "@/types/golfball";

export const golfBallApi = {
  getAll: () => apiClient.get<GolfBall[]>("/golf-balls"),

  getAvailable: () => apiClient.get<GolfBall[]>("/golf-balls/available"),

  getById: (id: number) => apiClient.get<GolfBall>(`/golf-balls/${id}`),

  getByBrand: (brand: string) =>
    apiClient.get<GolfBall[]>(`/golf-balls/brand/${brand}`),

  getByCondition: (condition: BallCondition) =>
    apiClient.get<GolfBall[]>(`/golf-balls/condition/${condition}`),

  filter: (params: {
    brand?: string;
    condition?: BallCondition;
    minPrice?: number;
    maxPrice?: number;
  }) => apiClient.get<GolfBall[]>("/golf-balls/filter", { params }),

  getBrands: () => apiClient.get<string[]>("/golf-balls/brands"),
};
```

## Cart Store Beispiel (Zustand)

```typescript
// store/cartStore.ts
import { create } from "zustand";
import { persist } from "zustand/middleware";
import { GolfBall } from "@/types/golfball";
import { CartItem } from "@/types/cart";

interface CartStore {
  items: CartItem[];
  addItem: (golfBall: GolfBall, quantity?: number) => void;
  removeItem: (golfBallId: number) => void;
  updateQuantity: (golfBallId: number, quantity: number) => void;
  clearCart: () => void;
  totalItems: () => number;
  totalAmount: () => number;
}

export const useCartStore = create<CartStore>()(
  persist(
    (set, get) => ({
      items: [],

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
            };
          }

          return { items: [...state.items, { golfBall, quantity }] };
        });
      },

      removeItem: (golfBallId) => {
        set((state) => ({
          items: state.items.filter((item) => item.golfBall.id !== golfBallId),
        }));
      },

      updateQuantity: (golfBallId, quantity) => {
        set((state) => ({
          items: state.items.map((item) =>
            item.golfBall.id === golfBallId ? { ...item, quantity } : item
          ),
        }));
      },

      clearCart: () => set({ items: [] }),

      totalItems: () =>
        get().items.reduce((sum, item) => sum + item.quantity, 0),

      totalAmount: () =>
        get().items.reduce(
          (sum, item) => sum + item.golfBall.price * item.quantity,
          0
        ),
    }),
    { name: "fairwayeco-cart" }
  )
);
```

## Ball-Zustände (für UI)

| Zustand    | Label    | Farbe (Tailwind) | Beschreibung   |
| ---------- | -------- | ---------------- | -------------- |
| `MINT`     | Mint     | `bg-green-500`   | Wie neu        |
| `GRADE_A`  | Grade A  | `bg-blue-500`    | Ausgezeichnet  |
| `GRADE_B`  | Grade B  | `bg-yellow-500`  | Gut            |
| `GRADE_C`  | Grade C  | `bg-orange-500`  | Übungsqualität |
| `PRACTICE` | Practice | `bg-gray-500`    | Driving Range  |

## Design-Hinweise

- **Primärfarbe**: Grün (Golf/Eco Theme)
- **Sekundärfarbe**: Blau (Wasser/See Theme)
- **Hero Section**: Golfplatz mit Wasserhindernis
- **Produktbilder**: Golfbälle mit Zustandsanzeige
- **Responsive**: Mobile-first Design

## Wichtige Funktionen

1. **Filter-Persistenz**: Filter in URL-Params speichern
2. **Cart-Persistenz**: LocalStorage mit Zustand persist
3. **Lazy Loading**: Bilder mit next/image
4. **Error Handling**: Toast-Notifications für API-Fehler
5. **Loading States**: Skeleton-Loading für bessere UX
