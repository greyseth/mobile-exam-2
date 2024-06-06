"use client";

import { useState } from "react";
import { post } from "../API";
import { useCookies } from "react-cookie";
import { useRouter } from "next/navigation";

import "../assets/css/login.css";

export default function LoginPage() {
  const [isLoading, setIsLoading] = useState(false);
  const [input, setInput] = useState({ email: "", password: "" });

  const [cookies, setCookies, removeCookies] = useCookies();
  const router = useRouter();

  async function handleSubmit() {
    const fetched = await post("/users/login", input);
    if (!fetched.error) {
      if (fetched.success) {
        setCookies(
          "login",
          { login_id: fetched.user_id, username: fetched.username },
          { path: "/" }
        );
        router.push("/");
      } else alert("Email/password tidak sesuai!");
    } else {
      alert("Sebuah Kesalahan Terjadi!");
      console.log(fetched.error);
    }
  }

  return (
    <>
      <section className="main-content centered">
        <h1>Authentication Page</h1>
        <div className="login-panel">
          <input
            type="text"
            value={input.email}
            onChange={(e) => setInput({ ...input, email: e.target.value })}
            placeholder="Email"
          />
          <input
            type="password"
            value={input.password}
            onChange={(e) => setInput({ ...input, password: e.target.value })}
            placeholder="Password"
          />
          <button onClick={handleSubmit} disabled={isLoading}>
            {isLoading ? "Mohon Menunggu..." : "Authentikasi"}
          </button>
        </div>
      </section>
    </>
  );
}
