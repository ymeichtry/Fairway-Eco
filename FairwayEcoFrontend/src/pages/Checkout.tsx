import { useMemo } from "react";
import { useNavigate, Link } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Separator } from "@/components/ui/separator";
import { useCartStore } from "@/store/cartStore";
import { ConditionBadge } from "@/components/products/ConditionBadge";
import { useToast } from "@/components/ui/use-toast";

const Checkout = () => {
  const navigate = useNavigate();
  const { toast } = useToast();
  const { items, totalAmount, clearCart } = useCartStore();

  const subtotal = useMemo(() => totalAmount(), [totalAmount]);
  const shipping = 0; // Placeholder shipping until backend rates exist
  const total = subtotal + shipping;

  const handlePlaceOrder = () => {
    if (items.length === 0) {
      toast({ title: "Cart is empty", description: "Please add items before checkout." });
      navigate("/products");
      return;
    }

    toast({
      title: "Order placed (demo)",
      description: "In a real flow this would create an order via the backend.",
    });
    clearCart();
    navigate("/");
  };

  return (
    <div className="max-w-4xl mx-auto px-4 py-10 space-y-8">
      <div className="flex items-center justify-between">
        <div>
          <p className="text-sm text-muted-foreground">Checkout</p>
          <h1 className="text-3xl font-display font-semibold">Review your order</h1>
        </div>
        <Button variant="outline" onClick={() => navigate(-1)}>
          Back
        </Button>
      </div>

      {items.length === 0 ? (
        <Card>
          <CardHeader>
            <CardTitle>Your cart is empty</CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <p className="text-muted-foreground">Add some golf balls to proceed.</p>
            <Button onClick={() => navigate("/products")}>Go to products</Button>
          </CardContent>
        </Card>
      ) : (
        <div className="grid md:grid-cols-3 gap-6">
          <Card className="md:col-span-2">
            <CardHeader>
              <CardTitle>Items</CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              {items.map((item) => (
                <div key={item.golfBall.id} className="flex gap-4 items-start">
                  <div className="w-16 h-16 rounded-lg bg-muted flex items-center justify-center flex-shrink-0">
                    <div className="w-10 h-10 rounded-full bg-gradient-to-br from-white to-gray-200 shadow" />
                  </div>
                  <div className="flex-1 min-w-0">
                    <div className="flex items-center justify-between">
                      <div>
                        <p className="text-xs text-muted-foreground">{item.golfBall.brand}</p>
                        <p className="font-medium text-foreground">{item.golfBall.model}</p>
                        <ConditionBadge condition={item.golfBall.condition} size="sm" />
                      </div>
                      <p className="font-display font-semibold">CHF {(item.golfBall.price * item.quantity).toFixed(2)}</p>
                    </div>
                    <p className="text-sm text-muted-foreground">Qty: {item.quantity}</p>
                  </div>
                </div>
              ))}
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle>Order summary</CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="flex justify-between text-sm">
                <span>Subtotal</span>
                <span>CHF {subtotal.toFixed(2)}</span>
              </div>
              <div className="flex justify-between text-sm">
                <span>Shipping</span>
                <span>{shipping === 0 ? "Included" : `CHF ${shipping.toFixed(2)}`}</span>
              </div>
              <Separator />
              <div className="flex justify-between text-lg font-semibold">
                <span>Total</span>
                <span>CHF {total.toFixed(2)}</span>
              </div>
              <Button className="w-full" onClick={handlePlaceOrder}>
                Place order (demo)
              </Button>
              <p className="text-xs text-muted-foreground">
                This is a demo checkout. Implement backend order creation to finalize purchases.
              </p>
            </CardContent>
          </Card>
        </div>
      )}
    </div>
  );
};

export default Checkout;
