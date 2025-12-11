import { useState, useMemo } from "react";
import { Search, SlidersHorizontal } from "lucide-react";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { ProductCard } from "@/components/products/ProductCard";
import { ProductFilter } from "@/components/products/ProductFilter";
import { mockGolfBalls, brands } from "@/data/mockProducts";
import { BallCondition } from "@/types/golfball";
import { cn } from "@/lib/utils";

const Products = () => {
  const [searchQuery, setSearchQuery] = useState("");
  const [selectedBrand, setSelectedBrand] = useState<string | null>(null);
  const [selectedCondition, setSelectedCondition] = useState<BallCondition | null>(null);
  const [showFilters, setShowFilters] = useState(false);

  const filteredProducts = useMemo(() => {
    return mockGolfBalls.filter((ball) => {
      const matchesSearch = 
        ball.brand.toLowerCase().includes(searchQuery.toLowerCase()) ||
        ball.model.toLowerCase().includes(searchQuery.toLowerCase());
      const matchesBrand = !selectedBrand || ball.brand === selectedBrand;
      const matchesCondition = !selectedCondition || ball.condition === selectedCondition;
      return matchesSearch && matchesBrand && matchesCondition;
    });
  }, [searchQuery, selectedBrand, selectedCondition]);

  return (
    <main className="min-h-screen bg-background pt-24 pb-20">
      <div className="container mx-auto px-4">
        {/* Header */}
        <div className="mb-8">
          <h1 className="font-display text-4xl md:text-5xl font-bold text-foreground mb-2">
            Shop Golf Balls
          </h1>
          <p className="text-muted-foreground">
            {filteredProducts.length} premium recycled balls available
          </p>
        </div>

        {/* Search & Filter Toggle */}
        <div className="flex flex-col md:flex-row gap-4 mb-6">
          <div className="relative flex-1">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-muted-foreground" />
            <Input
              type="search"
              placeholder="Search by brand or model..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="pl-10"
            />
          </div>
          <Button
            variant="outline"
            onClick={() => setShowFilters(!showFilters)}
            className={cn(showFilters && "bg-secondary")}
          >
            <SlidersHorizontal className="w-4 h-4 mr-2" />
            Filters
          </Button>
        </div>

        {/* Filters */}
        <div
          className={cn(
            "overflow-hidden transition-all duration-300",
            showFilters ? "max-h-96 opacity-100 mb-8" : "max-h-0 opacity-0"
          )}
        >
          <div className="p-6 bg-card rounded-xl border border-border">
            <ProductFilter
              brands={brands}
              selectedBrand={selectedBrand}
              selectedCondition={selectedCondition}
              onBrandChange={setSelectedBrand}
              onConditionChange={setSelectedCondition}
            />
          </div>
        </div>

        {/* Active Filters */}
        {(selectedBrand || selectedCondition) && (
          <div className="flex items-center gap-2 mb-6 animate-fade-in">
            <span className="text-sm text-muted-foreground">Active filters:</span>
            {selectedBrand && (
              <Button
                variant="secondary"
                size="sm"
                onClick={() => setSelectedBrand(null)}
                className="rounded-full"
              >
                {selectedBrand} ×
              </Button>
            )}
            {selectedCondition && (
              <Button
                variant="secondary"
                size="sm"
                onClick={() => setSelectedCondition(null)}
                className="rounded-full"
              >
                {selectedCondition.replace("_", " ")} ×
              </Button>
            )}
          </div>
        )}

        {/* Products Grid */}
        {filteredProducts.length > 0 ? (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
            {filteredProducts.map((product, index) => (
              <ProductCard key={product.id} product={product} index={index} />
            ))}
          </div>
        ) : (
          <div className="text-center py-20">
            <p className="text-muted-foreground text-lg">
              No products found matching your criteria.
            </p>
            <Button
              variant="ghost"
              className="mt-4"
              onClick={() => {
                setSearchQuery("");
                setSelectedBrand(null);
                setSelectedCondition(null);
              }}
            >
              Clear all filters
            </Button>
          </div>
        )}
      </div>
    </main>
  );
};

export default Products;
