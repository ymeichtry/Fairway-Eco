import { Droplets, TreePine, Target, Users } from "lucide-react";

const stats = [
  { value: "50,000+", label: "Balls Rescued" },
  { value: "15", label: "Partner Courses" },
  { value: "3,000+", label: "Happy Customers" },
  { value: "500kg", label: "Waste Prevented" },
];

const About = () => {
  return (
    <main className="min-h-screen bg-background pt-24 pb-20">
      {/* Hero Section */}
      <section className="relative py-20 overflow-hidden">
        <div className="absolute inset-0 bg-gradient-to-br from-primary/5 to-water-blue/5" />
        <div className="container mx-auto px-4 relative z-10">
          <div className="max-w-3xl mx-auto text-center">
            <h1 className="font-display text-4xl md:text-5xl lg:text-6xl font-bold text-foreground mb-6 animate-in">
              Golf Balls Deserve a{" "}
              <span className="text-gradient">Second Chance</span>
            </h1>
            <p className="text-lg text-muted-foreground leading-relaxed animate-in-delay-1">
              Every year, millions of golf balls end up lost in water hazards. 
              At Fairway Eco, we give these premium balls a new life – 
              saving you money while protecting our planet.
            </p>
          </div>
        </div>
      </section>

      {/* Stats */}
      <section className="py-12 border-y border-border">
        <div className="container mx-auto px-4">
          <div className="grid grid-cols-2 md:grid-cols-4 gap-8">
            {stats.map((stat, index) => (
              <div
                key={stat.label}
                className="text-center opacity-0 animate-fade-in-up"
                style={{ animationDelay: `${index * 0.1}s`, animationFillMode: "forwards" }}
              >
                <p className="font-display text-3xl md:text-4xl font-bold text-primary">
                  {stat.value}
                </p>
                <p className="text-muted-foreground text-sm mt-1">{stat.label}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Our Story */}
      <section className="py-20">
        <div className="container mx-auto px-4">
          <div className="grid md:grid-cols-2 gap-12 items-center">
            <div>
              <p className="text-primary font-medium mb-2">Our Story</p>
              <h2 className="font-display text-3xl md:text-4xl font-bold text-foreground mb-6">
                From Lake to Fairway
              </h2>
              <div className="space-y-4 text-muted-foreground">
                <p>
                  It started with a simple question: What happens to all those golf balls 
                  that land in the water? The answer shocked us – millions of premium 
                  balls sitting at the bottom of lakes, never to be played again.
                </p>
                <p>
                  We partnered with local Swiss golf courses to retrieve these balls, 
                  professionally clean and grade them, and offer them at prices that 
                  make golf more accessible for everyone.
                </p>
                <p>
                  Today, Fairway Eco is proud to be Switzerland's leading recycled 
                  golf ball retailer, combining quality, value, and environmental 
                  responsibility.
                </p>
              </div>
            </div>
            <div className="grid grid-cols-2 gap-4">
              <div className="space-y-4">
                <div className="p-6 bg-card rounded-2xl border border-border hover-lift">
                  <Droplets className="w-10 h-10 text-water-blue mb-4" />
                  <h3 className="font-display font-semibold text-foreground">Retrieved</h3>
                  <p className="text-sm text-muted-foreground mt-1">
                    Carefully collected from water hazards
                  </p>
                </div>
                <div className="p-6 bg-card rounded-2xl border border-border hover-lift">
                  <Target className="w-10 h-10 text-primary mb-4" />
                  <h3 className="font-display font-semibold text-foreground">Graded</h3>
                  <p className="text-sm text-muted-foreground mt-1">
                    Professionally inspected and sorted
                  </p>
                </div>
              </div>
              <div className="space-y-4 mt-8">
                <div className="p-6 bg-card rounded-2xl border border-border hover-lift">
                  <TreePine className="w-10 h-10 text-golf-green mb-4" />
                  <h3 className="font-display font-semibold text-foreground">Sustainable</h3>
                  <p className="text-sm text-muted-foreground mt-1">
                    Reducing waste, one ball at a time
                  </p>
                </div>
                <div className="p-6 bg-card rounded-2xl border border-border hover-lift">
                  <Users className="w-10 h-10 text-gold mb-4" />
                  <h3 className="font-display font-semibold text-foreground">Community</h3>
                  <p className="text-sm text-muted-foreground mt-1">
                    Serving Swiss golfers since 2020
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* CTA */}
      <section className="py-16 bg-secondary/50 rounded-3xl mx-4">
        <div className="container mx-auto px-4 text-center">
          <h2 className="font-display text-3xl font-bold text-foreground mb-4">
            Ready to Play Sustainable?
          </h2>
          <p className="text-muted-foreground mb-8 max-w-lg mx-auto">
            Join thousands of golfers who've made the switch to recycled balls. 
            Same quality, better price, greener choice.
          </p>
          <a
            href="/products"
            className="inline-flex items-center gap-2 px-8 py-4 bg-primary text-primary-foreground rounded-xl font-semibold hover:shadow-glow transition-all duration-300"
          >
            Shop Now
          </a>
        </div>
      </section>
    </main>
  );
};

export default About;
