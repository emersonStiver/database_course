export interface User  {
    id: number;
    name: string;
    email: string;
    phone: string;

}

export interface AuthResponse{
    accessToken: string;
}