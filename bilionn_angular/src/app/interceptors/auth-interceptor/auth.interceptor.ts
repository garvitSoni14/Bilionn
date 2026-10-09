import { HttpInterceptorFn } from '@angular/common/http';

const API_URL = 'http://localhost:8080';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  // Only touch requests to our own backend, never third-party URLs
  if (!req.url.startsWith(API_URL)) {
    return next(req);
  }

  // Requests are immutable, so clone with the change
  const authReq = req.clone({ withCredentials: true });
  return next(authReq);
};

// During Logging in
// Login component
//    │  authService.login({email, password})
//    ▼
// AuthService.login()  →  http.post('http://localhost:8080/api/v1.0/login', credentials)
//    │
//    ▼
// authInterceptor
//    │  URL starts with http://localhost:8080?  YES
//    │  req.clone({ withCredentials: true })
//    ▼
// loadingInterceptor
//    │  loadingService.show()   (counter 0 → 1, so loading() = true)
//    ▼
// Browser → Backend (:8080)
//    │  backend checks the password
//    │  responds 200 + Set-Cookie: JSESSIONID=... (or your token cookie)
//    ▼
// Browser stores the cookie (allowed only because withCredentials was true)
//    │
//    ▼
// loadingInterceptor: finalize → hide()   (counter 1 → 0, so loading() = false)
//    ▼
// AuthService: tap() saves user to currentUser signal + sessionStorage
//    ▼
// Login component: next → router.navigateByUrl('/dashboard')

// after logging in , other dashboard requests...
// Dashboard.loadDashboard()
//    │  http.get('.../dashboard')  and  http.get('.../orders/latest')
//    ▼
// authInterceptor  →  clone with withCredentials: true
//    ▼
// Browser automatically adds   Cookie: JSESSIONID=...   to the request
//    ▼
// Backend reads the cookie → knows who you are → returns data

// On app startup / page refresh...
// Dashboard.signOut() → AuthService.logout() → POST /logout (credentials on)
//    ▼
// Backend invalidates the session / clears the cookie
//    ▼
// tap(): currentUser = null, sessionStorage cleared
//    ▼
// Router → /login

// Load interceptor...
// Request jaati hai      → counter +1  → isLoading() = true
// Response/error aata hai → counter -1  → isLoading() = false (jab counter 0 ho)

