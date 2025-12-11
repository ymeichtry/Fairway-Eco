import { Link } from "react-router-dom";
import { Leaf, Mail, Phone, MapPin } from "lucide-react";

export function Footer() {
  return (
    <footer className="bg-foreground text-background mt-20">
      <div className="container mx-auto px-4 py-16">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-12">
          {/* Brand */}
          <div className="md:col-span-1">
            <div className="flex items-center gap-2 mb-4">
              <div className="w-10 h-10 rounded-full bg-primary flex items-center justify-center">
                <Leaf className="w-5 h-5 text-primary-foreground" />
              </div>
              <div className="flex flex-col">
                <span className="font-display text-xl font-bold">Fairway</span>
                <span className="text-xs text-primary font-medium -mt-1">ECO</span>
              </div>
            </div>
            <p className="text-background/70 text-sm leading-relaxed">
              Premium recycled golf balls rescued from water hazards. 
              Quality you can trust, prices you'll love.
            </p>
          </div>

          {/* Quick Links */}
          <div>
            <h4 className="font-display font-semibold text-lg mb-4">Shop</h4>
            <ul className="space-y-2">
              <li>
                <Link to="/products" className="text-background/70 hover:text-primary transition-colors text-sm">
                  All Products
                </Link>
              </li>
              <li>
                <Link to="/products?condition=MINT" className="text-background/70 hover:text-primary transition-colors text-sm">
                  Mint Condition
                </Link>
              </li>
              <li>
                <Link to="/products?brand=Titleist" className="text-background/70 hover:text-primary transition-colors text-sm">
                  Titleist
                </Link>
              </li>
              <li>
                <Link to="/products?brand=Callaway" className="text-background/70 hover:text-primary transition-colors text-sm">
                  Callaway
                </Link>
              </li>
            </ul>
          </div>

          {/* Company */}
          <div>
            <h4 className="font-display font-semibold text-lg mb-4">Company</h4>
            <ul className="space-y-2">
              <li>
                <Link to="/about" className="text-background/70 hover:text-primary transition-colors text-sm">
                  About Us
                </Link>
              </li>
              <li>
                <Link to="/sustainability" className="text-background/70 hover:text-primary transition-colors text-sm">
                  Sustainability
                </Link>
              </li>
              <li>
                <Link to="/faq" className="text-background/70 hover:text-primary transition-colors text-sm">
                  FAQ
                </Link>
              </li>
              <li>
                <Link to="/contact" className="text-background/70 hover:text-primary transition-colors text-sm">
                  Contact
                </Link>
              </li>
            </ul>
          </div>

          {/* Contact */}
          <div>
            <h4 className="font-display font-semibold text-lg mb-4">Contact</h4>
            <ul className="space-y-3">
              <li className="flex items-center gap-2 text-background/70 text-sm">
                <Mail className="w-4 h-4" />
                info@fairway-eco.ch
              </li>
              <li className="flex items-center gap-2 text-background/70 text-sm">
                <Phone className="w-4 h-4" />
                +41 79 123 45 67
              </li>
              <li className="flex items-start gap-2 text-background/70 text-sm">
                <MapPin className="w-4 h-4 mt-0.5" />
                <span>Golfstrasse 1<br />8000 Zürich, Schweiz</span>
              </li>
            </ul>
          </div>
        </div>

        <div className="border-t border-background/10 mt-12 pt-8 flex flex-col md:flex-row justify-between items-center gap-4">
          <p className="text-background/50 text-sm">
            © 2024 Fairway Eco. All rights reserved.
          </p>
          <div className="flex gap-6">
            <Link to="/privacy" className="text-background/50 hover:text-background text-sm transition-colors">
              Privacy
            </Link>
            <Link to="/terms" className="text-background/50 hover:text-background text-sm transition-colors">
              Terms
            </Link>
            <Link to="/imprint" className="text-background/50 hover:text-background text-sm transition-colors">
              Imprint
            </Link>
          </div>
        </div>
      </div>
    </footer>
  );
}
