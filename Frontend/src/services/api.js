import { BASE_URL } from '../api/apiClient';

// Utility: resolve a media/profile picture path returned by backend into a full URL.
// Backend sometimes returns relative paths like "/user/18" or "/api/user/18" — this helper
// ensures the browser requests the correct host:port by prefixing the API base if needed.
export function resolveMediaUrl(path) {
    if (!path) return null;
    try {
        // Already absolute URL
        const u = new URL(path, window.location.href);
        // If the path had a protocol (http/https), return as-is
        if (u.protocol === 'http:' || u.protocol === 'https:') return u.href;
        // Otherwise fall through and manually prefix BASE_URL
    } catch {
        // ignore
    }
    // If path starts with a leading slash, prefix with server origin + '/api' trimmed
    if (path.startsWith('/')) {
        // If path already contains '/api', just prefix origin
        if (path.startsWith('/api')) {
            return `${window.location.protocol}//${window.location.host}${path}`;
        }
        // otherwise prefix with BASE_URL which already contains /api
        return `${BASE_URL}${path.startsWith('/') ? '' : '/'}${path.replace(/^\//, '')}`;
    }
    // fallback: assume it's a relative path under API
    return `${BASE_URL}/${path}`;
}
