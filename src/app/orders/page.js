"use client";

import Image from "next/image";
import viewIcon from "../assets/img/view.svg";

import "../assets/css/tablelist.css";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { post } from "../API";

export default function ManageOrders() {
  const [orderData, setOrderData] = useState([]);
  const [isLoading, setIsLoading] = useState(true);

  const [fromDate, setFromDate] = useState("");
  const [toDate, setToDate] = useState("");
  const [search, setSearch] = useState("");

  async function fetchData(findId) {
    setIsLoading(true);

    const fetchUrl = findId
      ? "http://localhost:3000/orders/find/" + findId
      : "http://localhost:3000/orders/all";
    const request = await fetch(fetchUrl);
    const result = await request.json();

    setOrderData(result);
    setIsLoading(false);
  }

  async function dateFilter() {
    if (!fromDate || !toDate) return alert("Filter Tanggal Belum Terisi!");

    setIsLoading(true);

    const result = await post("/orders/finddate", {
      fromDate: fromDate,
      toDate: toDate,
    });

    setOrderData(result);
    setIsLoading(false);
  }

  useEffect(() => {
    fetchData();
  }, []);

  return (
    <section className="main-content">
      <h1>Pesanan Pengguna</h1>

      <div className="search-bar">
        <input
          type="text"
          placeholder="Cari Id Pesanan"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
        <button
          onClick={(e) => {
            fetchData(search);
          }}
        >
          Cari
        </button>
      </div>

      <div className="filter-bar">
        <div className="date-filter">
          <p>Dari Tanggal:</p>
          <input
            type="date"
            value={fromDate}
            onChange={(e) => setFromDate(e.target.value)}
          />
        </div>
        <div className="date-filter">
          <p>Sampai Tanggal:</p>
          <input
            type="date"
            value={toDate}
            onChange={(e) => setToDate(e.target.value)}
          />
        </div>
        <button
          className="hover"
          onClick={(e) => {
            dateFilter();
          }}
        >
          Filter Tanggal
        </button>
      </div>

      {isLoading ? <Loading /> : <OrdersTable orderData={orderData} />}
    </section>
  );
}

function Loading() {
  return <h1>Loading Orders...</h1>;
}

function OrdersTable({ orderData }) {
  const router = useRouter();

  return (
    <>
      {orderData.length > 0 ? (
        <table border={1}>
          <thead>
            <tr>
              <th>Id Pesanan</th>
              <th>Id Pengguna</th>
              <th>Pembayaran Pesanan (Rp.)</th>
              <th>Tanggal Pemesanan</th>
              <th>Status</th>
              <th>Detail</th>
            </tr>
          </thead>
          <tbody>
            {orderData.map((order, i) => {
              return (
                <tr key={i}>
                  <td>{order.order_id}</td>
                  <td>{order.user_id}</td>
                  <td>{order.total_price}</td>
                  <td>{order.date_ordered}</td>
                  <td>{order.status}</td>
                  <td className="row-actions">
                    <Image
                      src={viewIcon}
                      width={30}
                      height={30}
                      className="svg-red"
                      onClick={(e) => {
                        router.push("/orders/" + order.order_id);
                      }}
                    />
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      ) : (
        <h2>Tidak ada pesanan :(</h2>
      )}
    </>
  );
}
