import avro.schema
import avro.io
import io
import json
import os
from typing import Dict, Any

class AvroSerializer:
    def __init__(self, schema_file_path: str):
        """
        Initialize the Avro serializer with a schema file.
        
        Args:
            schema_file_path: Path to the .avsc schema file
        """
        self.schema = self._load_schema(schema_file_path)
        
    def _load_schema(self, schema_file_path: str) -> avro.schema.Schema:
        """Load Avro schema from file."""
        with open(schema_file_path, 'r') as schema_file:
            schema_dict = json.load(schema_file)
            return avro.schema.parse(json.dumps(schema_dict))
    
    def serialize(self, data: Dict[str, Any]) -> bytes:
        """
        Serialize data to Avro binary format.
        
        Args:
            data: Dictionary containing the data to serialize
            
        Returns:
            Serialized data as bytes
        """
        # Create a BytesIO buffer
        bytes_writer = io.BytesIO()
        
        # Create an encoder
        encoder = avro.io.BinaryEncoder(bytes_writer)
        
        # Create a writer
        writer = avro.io.DatumWriter(self.schema)
        
        # Write the data
        writer.write(data, encoder)
        
        # Get the serialized bytes
        return bytes_writer.getvalue()
    
    def deserialize(self, serialized_data: bytes) -> Dict[str, Any]:
        """
        Deserialize Avro binary data.
        
        Args:
            serialized_data: Serialized data as bytes
            
        Returns:
            Deserialized data as dictionary
        """
        # Create a BytesIO buffer from the serialized data
        bytes_reader = io.BytesIO(serialized_data)
        
        # Create a decoder
        decoder = avro.io.BinaryDecoder(bytes_reader)
        
        # Create a reader
        reader = avro.io.DatumReader(self.schema)
        
        # Read and return the data
        return reader.read(decoder)


def create_customer_serializer() -> AvroSerializer:
    """Create and return a customer event serializer."""
    schema_path = os.path.join(
        os.path.dirname(__file__), 
        '../',
        'schemas', 
        'customer-created-event.avsc'
    )
    return AvroSerializer(schema_path)
