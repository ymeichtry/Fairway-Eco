import { BallCondition, conditionConfig } from "@/types/golfball";
import { cn } from "@/lib/utils";

interface ConditionBadgeProps {
  condition: BallCondition;
  size?: "sm" | "md";
}

export function ConditionBadge({ condition, size = "md" }: ConditionBadgeProps) {
  const config = conditionConfig[condition];
  
  return (
    <span
      className={cn(
        "inline-flex items-center rounded-full font-medium",
        config.bgColor,
        config.color,
        size === "sm" ? "px-2 py-0.5 text-xs" : "px-3 py-1 text-sm"
      )}
    >
      {config.label}
    </span>
  );
}
