import { useQuery } from "@tanstack/react-query";
import { staffApi } from "../../api/staffApi";

export function useAvailableStaff(serviceId) {
  return useQuery({
    queryKey: ["availableStaff", serviceId],
    queryFn: () => staffApi.getStaffByService(serviceId),
    enabled: !!serviceId,
  });
}