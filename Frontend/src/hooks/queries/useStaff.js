import { useQuery } from "@tanstack/react-query";
import { salonApi } from "../../api/salonApi";

export function useStaff() {
  return useQuery({
    queryKey: ["staff"],
    queryFn: salonApi.getStaff,
  });
}