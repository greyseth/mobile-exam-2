"use client";

import { hostname, post } from "@/app/API";
import "../../assets/css/form.css";

import { useEffect, useState } from "react";

export default function AddUser() {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [admin, setAdmin] = useState(false);

  const [isLoading, setIsLoading] = useState(false);

  async function handleSubmit() {
    if (!username || !email || !password) {
      alert("Semua field harus terisi!");
      return;
    }

    setIsLoading(true);

    const addFetch = await post("/users/signup", {
      username: username,
      email: email,
      password: password,
      admin: admin,
    });

    if (!addFetch.error) {
      alert("User " + username + " telah dibuat");

      setUsername("");
      setEmail("");
      setPassword("");
    } else {
      if (addFetch.error === "existingemail") alert("Email sudah terdaftar!");
      else alert("Sebuah kesalahan terjadi!");
      console.log(addFetch);
    }

    setIsLoading(false);
  }

  return (
    <section className="main-content">
      <h1 style={{ marginBottom: "2em" }}>Tambahkan User</h1>
      {isLoading ? (
        <h1>Loading...</h1>
      ) : (
        <div className="form">
          <input
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            placeholder="Username pengguna"
          />
          <input
            type="text"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="Email pengguna"
          />
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="Password pengguna"
          />
          <div>
            <input
              id="adminCheckbox"
              type="checkbox"
              checked={admin}
              onChange={(e) => setAdmin(!admin)}
            />
            <label htmlFor="adminCheckbox">Is Admin</label>
          </div>

          <button onClick={handleSubmit}>Tambahkan</button>
        </div>
      )}
    </section>
  );
}
