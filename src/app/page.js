"use client";

import "./assets/css/welcome.css";

import { useRouter } from "next/navigation";
import { useState, useEffect } from "react";
import { useCookies } from "react-cookie";

export default function Home() {
  const [cookies, setCookies, removeCookies] = useCookies();
  const router = useRouter();

  useEffect(() => {
    if (!cookies.login) router.push("/auth");
  }, []);

  return (
    <>
      {cookies.login ? (
        <section className="main-content centered">
          <h1>Selamat datang, {cookies.login.username}</h1>
          <h3>
            Pilih salah satu menu dari sidebar untuk mulai mengelola data
            aplikasi
          </h3>
        </section>
      ) : null}
    </>
  );
}
