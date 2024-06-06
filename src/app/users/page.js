"use client";

import EditIcon from "../assets/img/edit.svg";
import DeleteIcon from "../assets/img/delete.svg";

import { useEffect, useState } from "react";
import "../assets/css/tablelist.css";
import { deleteRow, get } from "../API";
import Image from "next/image";

export default function UsersList() {
  const [userData, setUserData] = useState([]);
  const [isLoading, setIsLoading] = useState(true);

  async function fetchData() {
    setIsLoading(true);

    const fetched = await get("/users/all");
    setUserData(fetched);

    setIsLoading(false);
  }

  useEffect(() => {
    fetchData();
  }, []);

  return (
    <>
      <section className="main-content">
        <h1>List Users</h1>
        {isLoading ? (
          <Loading />
        ) : (
          <UsersTable fetchData={fetchData} userData={userData} />
        )}
      </section>
    </>
  );
}

function Loading() {
  return <h1>Loading data...</h1>;
}

function UsersTable({ fetchData, userData }) {
  async function handleDelete(userId) {
    const deleted = await deleteRow("/users/" + userId);
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
          <th>Id User</th>
          <th>Username</th>
          <th>Email</th>
          <th>Password</th>
          <th>Admin</th>
          <th>-</th>
        </tr>
      </thead>
      <tbody>
        {userData.map((user, i) => {
          return (
            <tr key={i}>
              <td>{user.user_id}</td>
              <td>{user.username}</td>
              <td>{user.email}</td>
              <td>{user.password}</td>
              <td>{user.admin === 0 ? "FALSE" : "TRUE"}</td>
              <td className="row-actions">
                {/* <Image
                  src={EditIcon}
                  width={30}
                  height={30}
                  className="svg-tertiary"
                /> */}
                <Image
                  src={DeleteIcon}
                  width={30}
                  height={30}
                  className="svg-red"
                  onClick={(e) => handleDelete(user.user_id)}
                />
              </td>
            </tr>
          );
        })}
      </tbody>
    </table>
  );
}
