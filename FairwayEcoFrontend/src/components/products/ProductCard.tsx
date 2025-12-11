import { useState } from "react";
import { Plus, Check } from "lucide-react";
import { GolfBall } from "@/types/golfball";
import { ConditionBadge } from "./ConditionBadge";
import { Button } from "@/components/ui/button";
import { useCartStore } from "@/store/cartStore";
import { cn } from "@/lib/utils";

interface ProductCardProps {
  product: GolfBall;
  index?: number;
}

export function ProductCard({ product, index = 0 }: ProductCardProps) {
  const [isAdded, setIsAdded] = useState(false);
  const { addItem } = useCartStore();

  const handleAddToCart = () => {
    addItem(product);
    setIsAdded(true);
    setTimeout(() => setIsAdded(false), 1500);
  };

  return (
    <div
      className={cn(
        "group relative bg-card rounded-2xl overflow-hidden border border-border/50 hover-lift",
        "opacity-0 animate-fade-in-up"
      )}
      style={{ animationDelay: `${index * 0.1}s`, animationFillMode: "forwards" }}
    >
      {/* Image */}
      <div className="relative aspect-square bg-gradient-to-br from-muted to-secondary overflow-hidden">
        <div className="absolute inset-0 flex items-center justify-center">
          {/* Golf ball visual */}
          <div className="relative w-32 h-32 rounded-full bg-gradient-to-br from-white via-gray-100 to-gray-200 shadow-lg transform group-hover:scale-110 transition-transform duration-500">
            <div className="absolute inset-2 rounded-full border-2 border-dashed border-gray-300/50" />
            <div className="absolute top-4 left-4 w-2 h-2 rounded-full bg-gray-300/70" />
            <div className="absolute top-6 right-6 w-1.5 h-1.5 rounded-full bg-gray-300/70" />
            <div className="absolute bottom-5 left-8 w-1.5 h-1.5 rounded-full bg-gray-300/70" />
          </div>
        </div>
        
        {/* Condition Badge */}
        <div className="absolute top-3 left-3">
          <ConditionBadge condition={product.condition} size="sm" />
        </div>

        {/* Quick Add Button */}
        <div className="absolute bottom-3 right-3 opacity-0 group-hover:opacity-100 transition-opacity duration-300">
          <Button
            size="icon"
            variant={isAdded ? "default" : "glass"}
            onClick={handleAddToCart}
            className={cn(
              "rounded-full w-10 h-10",
              isAdded && "bg-primary"
            )}
          >
            {isAdded ? (
              <Check className="w-4 h-4" />
            ) : (
              <Plus className="w-4 h-4" />
            )}
          </Button>
        </div>
      </div>

      {/* Content */}
      <div className="p-4">
        <div className="flex items-start justify-between gap-2">
          <div>
            <p className="text-xs text-muted-foreground uppercase tracking-wider">
              {product.brand}
            </p>
            <h3 className="font-display font-semibold text-foreground mt-0.5 group-hover:text-primary transition-colors">
              {product.model}
            </h3>
          </div>
          <div className="text-right">
            <p className="font-display font-bold text-lg text-primary">
              CHF {product.price.toFixed(2)}
            </p>
            <p className="text-xs text-muted-foreground">per ball</p>
          </div>
        </div>
        
        {product.quantity < 50 && (
          <p className="text-xs text-amber-600 mt-2">
            Only {product.quantity} left in stock
          </p>
        )}
      </div>
    </div>
  );
}
