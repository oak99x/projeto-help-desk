import React from 'react';
import ReactQuill from 'react-quill';
import 'quill/dist/quill.snow.css'; // Importa o CSS para o Quill

interface RichTextEditorProps {
  value: string;
  onChange: (value: string) => void;
}


const RichTextEditor: React.FC<RichTextEditorProps> = ({ value, onChange }) => {
  return (
    <div className="w-full">
      <ReactQuill
        theme="snow"
        value={value}
        onChange={onChange}
      />
    </div>
  );
}

export default RichTextEditor;
