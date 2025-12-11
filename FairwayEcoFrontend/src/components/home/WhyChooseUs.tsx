import { Droplets, BadgeCheck, Truck, Leaf } from "lucide-react";

const features = [
  {
    icon: Droplets,
    title: "Rescued from Water",
    description: "Every ball is carefully retrieved from Swiss golf course lakes and ponds.",
  },
  {
    icon: BadgeCheck,
    title: "Quality Graded",
    description: "Each ball is professionally cleaned, inspected, and graded for quality.",
  },
  {
    icon: Truck,
    title: "Fast Delivery",
    description: "Free shipping on orders over CHF 50. Usually delivered within 2-3 days.",
  },
  {
    icon: Leaf,
    title: "Eco-Friendly",
    description: "By choosing recycled, you're helping reduce waste and protect the environment.",
  },
];

export function WhyChooseUs() {
  return (
    <section className="py-20 bg-secondary/50">
      <div className="container mx-auto px-4">
        <div className="text-center max-w-2xl mx-auto mb-16">
          <p className="text-primary font-medium mb-2">Why Fairway Eco</p>
          <h2 className="font-display text-4xl font-bold text-foreground mb-4">
            Quality You Can Trust
          </h2>
          <p className="text-muted-foreground">
            We're committed to providing the best recycled golf balls while 
            making a positive impact on the environment.
          </p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8">
          {features.map((feature, index) => (
            <div
              key={feature.title}
              className="group text-center p-6 rounded-2xl bg-card border border-border/50 hover-lift opacity-0 animate-fade-in-up"
              style={{ animationDelay: `${index * 0.1}s`, animationFillMode: "forwards" }}
            >
              <div className="w-14 h-14 rounded-xl bg-primary/10 flex items-center justify-center mx-auto mb-4 group-hover:bg-primary group-hover:scale-110 transition-all duration-300">
                <feature.icon className="w-7 h-7 text-primary group-hover:text-primary-foreground transition-colors" />
              </div>
              <h3 className="font-display font-semibold text-lg text-foreground mb-2">
                {feature.title}
              </h3>
              <p className="text-sm text-muted-foreground">
                {feature.description}
              </p>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}
