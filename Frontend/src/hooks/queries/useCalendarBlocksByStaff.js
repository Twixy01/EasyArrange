import { useQuery } from '@tanstack/react-query'
import { calendarApi } from '../../api/calendarApi'

export function useCalendarBlocksByStaff(staffId) {
  return useQuery({
    queryKey: ['calendarBlocks', staffId],
    queryFn: () => calendarApi.getCalendarBlocksByStaff(staffId),
    enabled: !!staffId
  })
}