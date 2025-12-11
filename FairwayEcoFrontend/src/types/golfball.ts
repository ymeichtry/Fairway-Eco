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

export const conditionConfig: Record<BallCondition, { label: string; color: string; bgColor: string }> = {
  MINT: { label: "Mint", color: "text-emerald-700", bgColor: "bg-emerald-100" },
  GRADE_A: { label: "Grade A", color: "text-blue-700", bgColor: "bg-blue-100" },
  GRADE_B: { label: "Grade B", color: "text-amber-700", bgColor: "bg-amber-100" },
  GRADE_C: { label: "Grade C", color: "text-orange-700", bgColor: "bg-orange-100" },
  PRACTICE: { label: "Practice", color: "text-slate-700", bgColor: "bg-slate-100" },
};
