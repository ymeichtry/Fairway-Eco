import { Link } from "react-router-dom";
import { ArrowRight, Recycle, Leaf, Award } from "lucide-react";
import { Button } from "@/components/ui/button";

export function Hero() {
  return (
    <section className="relative min-h-screen flex items-center overflow-hidden">
      {/* Background */}
      <div className="absolute inset-0 bg-gradient-to-br from-fairway via-background to-secondary" />
      
      {/* Animated circles */}
      <div className="absolute inset-0 overflow-hidden">
        <div className="absolute -top-40 -right-40 w-96 h-96 rounded-full bg-primary/10 blur-3xl animate-pulse" />
        <div className="absolute -bottom-40 -left-40 w-96 h-96 rounded-full bg-water-blue/10 blur-3xl animate-pulse" style={{ animationDelay: "1s" }} />
      </div>

      {/* Content */}
      <div className="container mx-auto px-4 relative z-10 pt-20">
        <div className="grid lg:grid-cols-2 gap-12 items-center">
          {/* Text Content */}
          <div className="max-w-xl">
            <div className="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-primary/10 text-primary text-sm font-medium mb-6 animate-in">
              <Recycle className="w-4 h-4" />
              <span>Recycled from Swiss Lakes</span>
            </div>
            
            <h1 className="font-display text-5xl md:text-6xl lg:text-7xl font-bold text-foreground leading-tight animate-in-delay-1">
              Premium Golf Balls,{" "}
              <span className="text-gradient">Second Life</span>
            </h1>
            
            <p className="text-lg text-muted-foreground mt-6 leading-relaxed animate-in-delay-2">
              Discover top-brand golf balls rescued from water hazards. 
              Same quality, fraction of the price, better for the planet.
            </p>

            <div className="flex flex-col sm:flex-row gap-4 mt-8 animate-in-delay-3">
              <Button variant="hero" size="xl" asChild>
                <Link to="/products">
                  Shop Now
                  <ArrowRight className="w-5 h-5" />
                </Link>
              </Button>
              <Button variant="glass" size="xl" asChild>
                <Link to="/about">Learn More</Link>
              </Button>
            </div>

            {/* Stats */}
            <div className="grid grid-cols-3 gap-6 mt-12 pt-8 border-t border-border animate-in-delay-4">
              <div>
                <p className="font-display text-3xl font-bold text-foreground">50K+</p>
                <p className="text-sm text-muted-foreground">Balls Rescued</p>
              </div>
              <div>
                <p className="font-display text-3xl font-bold text-foreground">-70%</p>
                <p className="text-sm text-muted-foreground">vs. New Price</p>
              </div>
              <div>
                <p className="font-display text-3xl font-bold text-foreground">4.9★</p>
                <p className="text-sm text-muted-foreground">Customer Rating</p>
              </div>
            </div>
          </div>

          {/* Visual */}
          <div className="relative hidden lg:flex items-center justify-center">
            <div className="relative">
              {/* Main Ball */}
              <div className="w-72 h-72 rounded-full bg-gradient-to-br from-white via-gray-100 to-gray-200 shadow-2xl animate-float">
                <div className="absolute inset-4 rounded-full border-4 border-dashed border-gray-300/50" />
                <div className="absolute inset-0 flex items-center justify-center">
                  <div className="text-center">
                    <Leaf className="w-12 h-12 text-primary mx-auto" />
                    <p className="font-display font-bold text-foreground mt-2">ECO</p>
                  </div>
                </div>
              </div>

              {/* Floating Elements */}
              <div className="absolute -top-8 -left-8 w-16 h-16 rounded-full bg-primary/20 flex items-center justify-center animate-bounce-subtle">
                <Award className="w-8 h-8 text-primary" />
              </div>
              <div className="absolute -bottom-4 -right-4 w-20 h-20 rounded-full bg-water-blue/20 flex items-center justify-center animate-bounce-subtle" style={{ animationDelay: "0.5s" }}>
                <Recycle className="w-10 h-10 text-water-blue" />
              </div>

              {/* Water ripple effect */}
              <div className="absolute -bottom-20 left-1/2 -translate-x-1/2 w-48 h-12 bg-water-blue/10 rounded-full blur-md" />
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}
