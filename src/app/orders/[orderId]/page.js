"use client";

import { post } from "@/app/API";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";

export default function OrderDetails({ params }) {
  const [orderDetail, setOrderDetail] = useState();
  const [orderItems, setOrderItems] = useState([]);

  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(false);

  useEffect(() => {
    async function fetchData() {
      const request = await fetch(
        "http://localhost:3000/orders/" + params.orderId
      );
      const result = await request.json();

      if (!result.empty && !result.error) {
        setOrderDetail(result.data);

        const request2 = await fetch(
          "http://localhost:3000/orders/detail/" + params.orderId
        );
        const result2 = await request2.json();

        if (!result2.error) {
          setOrderItems(result2.data);
          setIsLoading(false);
        } else setError(true);
      } else setError(true);
    }

    fetchData();
  }, []);

  return (
    <section className="main-content">
      <h1>Pesanan {params.orderId}</h1>

      {isLoading ? (
        <Loading />
      ) : error ? (
        <Error />
      ) : (
        <OrderContent
          orderDetail={orderDetail}
          setOrderDetail={setOrderDetail}
          orderItems={orderItems}
        />
      )}
    </section>
  );
}

function Loading() {
  return <h1>Loading Pesanan...</h1>;
}

function Error() {
  return <h1>Sebuah kesalahan terjadi...</h1>;
}

function OrderContent({ orderDetail, setOrderDetail, orderItems }) {
  const router = useRouter();

  async function handleConfirm() {
    console.log(orderDetail);

    const result = await post("/orders/confirm/" + orderDetail.order_id, {
      status: "terkonfirmasi",
    });
    if (result.error) alert("Sebuah kesalahan terjadi");
    else {
      setOrderDetail({ ...orderDetail, status: "terkonfirmasi" });
    }
  }

  return (
    <div>
      <p>Id User Pemesan: {orderDetail.user_id}</p>
      <p>Tanggal Pemesanan: {orderDetail.date_ordered}</p>
      <p>Harga Pemesanan: Rp. {orderDetail.total_price}</p>

      <OrderItems orderItems={orderItems} />

      <h2 style={{ marginBottom: "1em" }}>
        Status Pesanan: {orderDetail.status.toUpperCase()}
      </h2>
      {orderDetail.status !== "terkonfirmasi" ? (
        <button
          onClick={handleConfirm}
          style={{
            backgroundColor: "var(--primary-color)",
            padding: "1em",
            color: "var(--tertiary-color)",
            borderRadius: "10px",
            border: "none",
            marginRight: "1.5em",
          }}
        >
          Konfirmasi Pembayaran
        </button>
      ) : null}

      <button
        onClick={(e) => router.push("/orders")}
        style={{
          backgroundColor: "var(--primary-color)",
          padding: "1em",
          color: "var(--tertiary-color)",
          borderRadius: "10px",
          border: "none",
          marginRight: "1.5em",
        }}
      >
        Kembali ke List
      </button>
    </div>
  );
}

function OrderItems({ orderItems }) {
  return (
    <table border={1} style={{ marginBottom: "2em" }}>
      <thead>
        <tr>
          <th>Id Barang</th>
          <th>Nama Barang</th>
          <th>Harga Satuan (Rp.)</th>
          <th>Kuantias</th>
        </tr>
      </thead>
      <tbody>
        {orderItems.map((item, i) => {
          return (
            <tr key={i}>
              <td>{item.item_id}</td>
              <td>{item.name}</td>
              <td>{item.price}</td>
              <td>{item.quantity}</td>
            </tr>
          );
        })}
      </tbody>
    </table>
  );
}
