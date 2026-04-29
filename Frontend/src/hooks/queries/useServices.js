import { useQuery } from "@tanstack/react-query";
import { serviceApi } from "../../api/serviceApi";

export function useServices() {
  return useQuery({
    queryKey: ["services"],
    queryFn: serviceApi.getServices,
  });
}