import { useQuery } from "@tanstack/react-query";
import { salonApi } from "../../api/salonApi";

export function useAvailableStaff(serviceId) {
  return useQuery({
    queryKey: ["availableStaff", serviceId],
    queryFn: () => salonApi.getStaffByService(serviceId),
    enabled: !!serviceId,
  });
}