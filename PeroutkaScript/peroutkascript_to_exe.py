import compiler

# Zdrojový kód k překladu do souboru out.exe
source_code = """
   print ahoj
   add 5 5
"""

compiled_code = source_code.encode(), compiler.compile_and_execute(source_code)
output_file = "/bin/out.exe"

with open(output_file, 'wb') as file:
        file.write(compiled_code)
        print("Soubor úspěšně zapsán!")



