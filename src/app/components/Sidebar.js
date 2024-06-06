"use client";

import { useRouter } from "next/navigation";
import { useCookies } from "react-cookie";

const buttons = [
  { display: "Manage Items", url: "/items" },
  { display: "Manage Users", url: "/users" },
  { display: "Manage Orders", url: "/orders" },
  { display: "Create Item", url: "/add/item" },
  { display: "Create User", url: "/add/user" },
];

export default function Sidebar() {
  const router = useRouter();
  const [cookies, setCookies, removeCookies] = useCookies();

  return (
    <>
      <aside className="sidebar">
        {cookies.login
          ? buttons.map((b, i) => {
              return (
                <button
                  onClick={(e) => {
                    router.push(b.url);
                  }}
                  key={i}
                >
                  {b.display}
                </button>
              );
            })
          : null}
      </aside>
    </>
  );
}
