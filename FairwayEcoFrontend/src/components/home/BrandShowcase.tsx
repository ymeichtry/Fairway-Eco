import { brands } from "@/data/mockProducts";

export function BrandShowcase() {
  return (
    <section className="py-16 bg-background border-y border-border">
      <div className="container mx-auto px-4">
        <p className="text-center text-muted-foreground mb-8">
          Premium brands at a fraction of the cost
        </p>
        
        <div className="flex flex-wrap items-center justify-center gap-8 md:gap-16">
          {brands.map((brand, index) => (
            <div
              key={brand}
              className="font-display text-2xl md:text-3xl font-bold text-foreground/20 hover:text-primary transition-colors duration-300 cursor-pointer opacity-0 animate-fade-in"
              style={{ animationDelay: `${index * 0.1}s`, animationFillMode: "forwards" }}
            >
              {brand}
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}
