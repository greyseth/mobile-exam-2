"use client";

import EditIcon from "../assets/img/edit.svg";
import DeleteIcon from "../assets/img/delete.svg";

import { useEffect, useState } from "react";
import "../assets/css/tablelist.css";
import { deleteRow, get, hostname } from "../API";
import Link from "next/link";
import Image from "next/image";
import { useRouter } from "next/navigation";

export default function ItemsList() {
  const [itemData, setItemData] = useState([]);
  const [isLoading, setIsLoading] = useState(true);

  async function fetchData() {
    setIsLoading(true);

    const fetched = await get("/items");
    setItemData(fetched);

    setIsLoading(false);
  }

  useEffect(() => {
    fetchData();
  }, []);

  return (
    <>
      <section className="main-content">
        <h1>List Barang</h1>
        {isLoading ? (
          <Loading />
        ) : (
          <ItemsTable fetchData={fetchData} itemData={itemData} />
        )}
      </section>
    </>
  );
}

function Loading() {
  return <h1>Loading data...</h1>;
}

function ItemsTable({ fetchData, itemData }) {
  const router = useRouter();

  async function handleDelete(itemId) {
    const deleted = await deleteRow("/items/" + itemId);
    if (deleted.success) fetchData();
    else {
      alert("Sebuah kesalahan terjadi!");
      console.log(deleted);
    }
  }

  return (
    <table border={1}>
      <thead>
        <tr>
          <th>Nama</th>
          <th>Deskripsi</th>
          <th>Harga (Rp.)</th>
          <th>Jumlah Dipesan</th>
          <th>Gambar (Link)</th>
          <th>-</th>
        </tr>
      </thead>
      <tbody>
        {itemData.map((item, i) => {
          return (
            <tr key={i}>
              <td>{item.name}</td>
              <td>{item.description}</td>
              <td>{item.price}</td>
              <td>{item.orders_count}</td>
              <td>
                {item.image ? (
                  <Link
                    href={hostname + "/images/" + item.image}
                    target="_blank"
                  >
                    Buka
                  </Link>
                ) : (
                  <p>Tidak Memliki Gambar</p>
                )}
              </td>
              <td className="row-actions">
                <Image
                  src={EditIcon}
                  width={30}
                  height={30}
                  className="svg-tertiary"
                  onClick={(e) => {
                    router.push("/edit/item/" + item.item_id);
                  }}
                />
                <Image
                  src={DeleteIcon}
                  width={30}
                  height={30}
                  className="svg-red"
                  onClick={(e) => handleDelete(item.item_id)}
                />
              </td>
            </tr>
          );
        })}
      </tbody>
    </table>
  );
}
