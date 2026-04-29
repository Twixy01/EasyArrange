import { useQuery } from "@tanstack/react-query";
import { staffApi } from "../../api/staffApi";

export function useStaff() {
  return useQuery({
    queryKey: ["staff"],
    queryFn: staffApi.getStaff,
  });
}