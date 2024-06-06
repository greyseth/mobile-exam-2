//data: [{value (state), setValue (setState), type (string), placeholder (string)}]
//buttons: [{display (string), action (function)}]

//Guhh was gonna make a way to just be able to loop through all the states to make a form but NOOOOOO i have to do it the manual way
//yeah fuck you, too, React
export default function PageForm({ data, buttons }) {
  return (
    <div>
      {data.map((d, i) => {
        return (
          <input
            key={i}
            value={d.value}
            onChange={(e) => d.setValue(e)}
            type={d.type}
            placeholder={d.placeholder}
          />
        );
      })}

      {buttons.map((b, i) => {
        return (
          <button key={i} onClick={b.action}>
            {b.display}
          </button>
        );
      })}
    </div>
  );
}
