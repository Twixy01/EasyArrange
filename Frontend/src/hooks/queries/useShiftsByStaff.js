import { useQuery } from "@tanstack/react-query";
import { salonApi } from "../../api/salonApi";

export function useShiftsByStaff(staffId) {
  return useQuery({
    queryKey: ["shiftsByStaff", staffId],
    queryFn: () => salonApi.getShiftsByStaff(staffId),
    enabled: !!staffId,
  });
}