import { useQuery } from "@tanstack/react-query";
import { shiftApi } from "../../api/shiftApi";

export function useShiftsByStaff(staffId) {
  return useQuery({
    queryKey: ["shiftsByStaff", staffId],
    queryFn: () => shiftApi.getShiftsByStaff(staffId),
    enabled: !!staffId,
  });
}