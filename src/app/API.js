const hostname = "http://localhost:3000";

async function get(route) {
  const request = await fetch(hostname + route);
  const result = await request.json();
  return result;
}

async function post(route, body) {
  const request = await fetch(hostname + route, {
    method: "POST",
    headers: {
      Accept: "application/json",
      "Content-Type": "application/json",
    },
    body: JSON.stringify(body),
  });
  const result = await request.json();
  return result;
}

async function deleteRow(route) {
  const request = await fetch(hostname + route, {
    method: "DELETE",
  });
  const result = await request.json();
  return result;
}

async function put(route, body) {
  const request = await fetch(hostname + route, {
    method: "PUT",
    body: JSON.stringify(body),
  });
  const result = await request.json();
  return result;
}

export { get, post, deleteRow, put, hostname };
