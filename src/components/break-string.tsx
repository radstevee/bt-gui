import React from "react";

interface BreakStringProps {
  text: string
}

const BreakString = ({ text }: BreakStringProps) => {
  const strArray = text.split("\n");
  return (
    <>
      {strArray.map((str, index) => (
        <React.Fragment key={index}>
          {str}
          {index !== str.length - 1 && <br />}
        </React.Fragment>
      ))}
    </>
  );
}

export default BreakString;
