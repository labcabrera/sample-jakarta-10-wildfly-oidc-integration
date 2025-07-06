import io
import avro.schema
import avro.io
import json

class AvroObjectSerializer:
    def __init__(self, schema_path_or_dict, schema_id=1):
        if isinstance(schema_path_or_dict, str):
            with open(schema_path_or_dict, 'r') as f:
                schema_str = f.read()
        else:
            schema_str = json.dumps(schema_path_or_dict)
        
        self.schema = avro.schema.parse(schema_str)
        self.schema_id = schema_id

    def serialize(self, obj: dict) -> bytes:
        """Serializa un objeto dict en bytes usando formato Schema Registry."""
        
        # ✅ PASO 1: Serializar con Avro
        writer = avro.io.DatumWriter(self.schema)
        bytes_writer = io.BytesIO()
        encoder = avro.io.BinaryEncoder(bytes_writer)
        writer.write(obj, encoder)
        avro_data = bytes_writer.getvalue()
        
        # ✅ PASO 2: Crear formato Schema Registry
        # FORMATO: [magic_byte][schema_id_4_bytes][avro_data]
        result_buffer = io.BytesIO()
        
        # Magic byte (siempre 0x00)
        result_buffer.write(b'\x00')
        
        # Schema ID (4 bytes, big-endian)
        result_buffer.write(self.schema_id.to_bytes(4, byteorder='big'))
        
        # Datos Avro
        result_buffer.write(avro_data)
        
        return result_buffer.getvalue()