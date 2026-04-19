import { useQuery } from '@tanstack/react-query'
import { salonApi } from '../../api/salonApi'

export function useCalendarBlocksByStaff(staffId) {
  return useQuery({
    queryKey: ['calendarBlocks', staffId],
    queryFn: () => salonApi.getCalendarBlocksByStaff(staffId),
    enabled: !!staffId
  })
}