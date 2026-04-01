// import { createContext, useEffect, useMemo, useState } from "react";
// import { salonApi } from "../services/api";

// export const AuthContext = createContext(null);

// export function AuthProvider({ children }) {
//   const [users, setUsers] = useState(() => {
//     const saved = localStorage.getItem("salon-users");
//     return saved ? JSON.parse(saved) : defaultUsers;
//   });

//   const [currentUser, setCurrentUser] = useState(() => {
//     const saved = localStorage.getItem("salon-current-user");
//     return saved ? JSON.parse(saved) : null;
//   });

//   useEffect(() => {
//     localStorage.setItem("salon-users", JSON.stringify(users));
//   }, [users]);

//   useEffect(() => {
//     localStorage.setItem("salon-current-user", JSON.stringify(currentUser));
//   }, [currentUser]);

//   const login = async ({ email, password }) => {
//     try {
//       const user = await salonApi.loginUser({ email, password });
//       setCurrentUser(user);
//       return user;
//     } catch {
//       const localUser = users.find(
//         (item) => item.email === email && item.password === password
//       );
//       if (!localUser) {
//         throw new Error("Invalid email or password.");
//       }
//       setCurrentUser(localUser);
//       return localUser;
//     }
//   };

//   const register = ({ name, email, password }) => {
//     const exists = users.some((item) => item.email === email);
//     if (exists) {
//       throw new Error("An account with that email already exists.");
//     }
//     const newUser = {
//       id: Date.now(),
//       userId: Date.now(),
//       name,
//       email,
//       password,
//       profilePicture: "",
//       role: "CUSTOMER",
//     };
//     const updated = [...users, newUser];
//     setUsers(updated);
//     setCurrentUser(newUser);
//     return newUser;
//   };

//   const logout = () => {
//     setCurrentUser(null);
//   };

//   const updateProfile = (payload) => {
//     if (!currentUser) return;

//     const updatedUser = { ...currentUser, ...payload };
//     setCurrentUser(updatedUser);
//     setUsers((prev) =>
//       prev.map((user) => (user.id === updatedUser.id ? updatedUser : user))
//     );
//   };

//   const value = useMemo(
//     () => ({
//       users,
//       currentUser,
//       role: currentUser?.role || null,
//       isStaff: currentUser?.role === "STAFF",
//       isCustomer: currentUser?.role === "CUSTOMER",
//       isAuthenticated: !!currentUser,
//       login,
//       register,
//       logout,
//       updateProfile,
//     }),
//     [users, currentUser]
//   );

//   return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
// }