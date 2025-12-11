import { Hero } from "@/components/home/Hero";
import { FeaturedProducts } from "@/components/home/FeaturedProducts";
import { WhyChooseUs } from "@/components/home/WhyChooseUs";
import { BrandShowcase } from "@/components/home/BrandShowcase";
import { Newsletter } from "@/components/home/Newsletter";

const Index = () => {
  return (
    <main>
      <Hero />
      <BrandShowcase />
      <FeaturedProducts />
      <WhyChooseUs />
      <Newsletter />
    </main>
  );
};

export default Index;
