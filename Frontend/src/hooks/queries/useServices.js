import { useQuery } from "@tanstack/react-query";
import { salonApi } from "../../api/salonApi";

export function useServices() {
  return useQuery({
    queryKey: ["services"],
    queryFn: salonApi.getServices,
  });
}