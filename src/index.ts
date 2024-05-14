import RazerPayReactNativeModule from "./RazerPayReactNativeModule";

export function pay(paymentDetails: Record<string, string>): string {
  const paymentDetailsStr = JSON.stringify(paymentDetails);
  return RazerPayReactNativeModule.pay(paymentDetailsStr);
}
