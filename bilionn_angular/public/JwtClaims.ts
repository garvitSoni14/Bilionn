export interface JwtClaims {
  userId: number;
  name: string;
  role: string;
  sub: string;   // email
  exp: number;   // seconds since epoch
}