import { BallCondition, conditionConfig } from "@/types/golfball";
import { Button } from "@/components/ui/button";
import { cn } from "@/lib/utils";

interface ProductFilterProps {
  brands: string[];
  selectedBrand: string | null;
  selectedCondition: BallCondition | null;
  onBrandChange: (brand: string | null) => void;
  onConditionChange: (condition: BallCondition | null) => void;
}

const conditions: BallCondition[] = ["MINT", "GRADE_A", "GRADE_B", "GRADE_C", "PRACTICE"];

export function ProductFilter({
  brands,
  selectedBrand,
  selectedCondition,
  onBrandChange,
  onConditionChange,
}: ProductFilterProps) {
  return (
    <div className="space-y-6">
      {/* Brand Filter */}
      <div>
        <h3 className="font-display font-semibold text-sm mb-3 text-foreground">Brand</h3>
        <div className="flex flex-wrap gap-2">
          <Button
            variant={selectedBrand === null ? "default" : "outline"}
            size="sm"
            onClick={() => onBrandChange(null)}
            className="rounded-full"
          >
            All
          </Button>
          {brands.map((brand) => (
            <Button
              key={brand}
              variant={selectedBrand === brand ? "default" : "outline"}
              size="sm"
              onClick={() => onBrandChange(brand)}
              className="rounded-full"
            >
              {brand}
            </Button>
          ))}
        </div>
      </div>

      {/* Condition Filter */}
      <div>
        <h3 className="font-display font-semibold text-sm mb-3 text-foreground">Condition</h3>
        <div className="flex flex-wrap gap-2">
          <Button
            variant={selectedCondition === null ? "default" : "outline"}
            size="sm"
            onClick={() => onConditionChange(null)}
            className="rounded-full"
          >
            All
          </Button>
          {conditions.map((condition) => (
            <Button
              key={condition}
              variant={selectedCondition === condition ? "default" : "outline"}
              size="sm"
              onClick={() => onConditionChange(condition)}
              className={cn(
                "rounded-full",
                selectedCondition === condition && conditionConfig[condition].bgColor
              )}
            >
              {conditionConfig[condition].label}
            </Button>
          ))}
        </div>
      </div>
    </div>
  );
}
