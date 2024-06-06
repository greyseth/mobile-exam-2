import { Inter } from "next/font/google";
import "./assets/css/globals.css";
import Header from "./components/Header";
import Sidebar from "./components/Sidebar";
import { useCookies } from "react-cookie";

const inter = Inter({ subsets: ["latin"] });

export const metadata = {
  title: "RetroEmporium CMS",
  description: "Aplikasi CMS untuk Pemrograman Perangkat Bergerak",
};

export default function RootLayout({ children }) {
  return (
    <html lang="en">
      <body className={inter.className}>
        <Header />
        <div className="content">
          <Sidebar />
          {children}
        </div>
      </body>
    </html>
  );
}
