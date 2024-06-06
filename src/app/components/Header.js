"use client";

import Image from "next/image";
import AccountIcon from "../assets/img/account.svg";
import { useRouter } from "next/navigation";
import { useCookies } from "react-cookie";

export default function Header() {
  const router = useRouter();
  const [cookies, setCookies, removeCookies] = useCookies();

  return (
    <div className="header">
      <h2>RetroEmporium CMS</h2>
      <div
        className="account-btn hover"
        onClick={(e) => {
          removeCookies("login");
          router.push("/auth");
        }}
      >
        <Image src={AccountIcon} />
      </div>
    </div>
  );
}
