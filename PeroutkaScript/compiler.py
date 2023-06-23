import os

# Globální proměnné
variables = {}

# Funkce pro načítání souboru s koncovkou .pts
def load_file(filename):
    # Získání cesty k souboru ve stejné složce jako tento skript
    script_dir = os.path.dirname(os.path.abspath(__file__))
    file_path = os.path.join(script_dir, filename)

    # Ověření existence souboru
    if os.path.isfile(file_path):
        with open(file_path, 'r') as file:
            code = file.read()
        return code
    else:
        raise FileNotFoundError("Soubor {} nebyl nalezen.".format(filename))

# Funkce pro zpracování instrukce add
def process_add(args):
    result = None
    for arg in args:
        if isinstance(arg, int):
            if result is None:
                result = arg
            else:
                result += arg
    return result

# Funkce pro zpracování instrukce print
def process_print(args):
    for arg in args:
        if isinstance(arg, str):
            # Výpis řetězců
            print(arg)
        elif isinstance(arg, tuple):
            # Výpis hodnot proměnných
            var_name, var_value = arg
            print(var_name + " = " + str(var_value))
        else:
            print(arg)

# Funkce pro zpracování instrukce input
def process_input():
    return input("Vstup: ")

# Funkce pro zpracování instrukce output
def process_output(value):
    if value == 0:
        print("Výstup je 0")
    elif value == 1:
        print("Výstup je 1")
    else:
        print("Neznámý výstup")

# Funkce pro zpracování instrukce getlib
def process_getlib(lib_name):
    lib_code = load_file(lib_name + ".ptslib")
    compile_and_execute(lib_code)

# Funkce pro zpracování instrukce function
def process_function(name, params, body):
    def function_impl(args):
        # Vytvoření nového lokálního kontextu s proměnnými
        local_variables = {}
        for i, param in enumerate(params):
            local_variables[param] = args[i]

        # Vykonání těla funkce v novém kontextu
        compile_and_execute(body, local_variables)

    # Uložení funkce do globálních proměnných
    variables[name] = function_impl

# Funkce pro kompilaci a vykonání kódu
def compile_and_execute(code, variables=None):
    if variables is None:
        variables = {}

    # Přidání globálních proměnných do lokálního kontextu
    local_variables = variables.copy()
    local_variables.update(variables)

    # Rozdělení kódu na řádky
    lines = code.split("\n")

    # Procházení řádků kódu
    for line in lines:
        # Rozdělení řádku na instrukci a argumenty
        parts = line.split(" ")
        instruction = parts[0]
        arguments = parts[1:]

        # Zpracování instrukce
        if instruction == "add":
            result = process_add([int(arg) for arg in arguments])
            print("Výsledek: {}".format(result))
        elif instruction == "print":
            process_print(arguments)
        elif instruction == "input":
            result = process_input()
            print("Vstupní hodnota: {}".format(result))
        elif instruction == "output":
            process_output(int(arguments[0]))
        elif instruction == "getlib":
            process_getlib(arguments[0])
        elif instruction == "function":
            # Zpracování definice funkce
            name = arguments[0]
            params = arguments[1:-1]  # Odstranění závorky
            body = " ".join(arguments[-1:])  # Získání těla funkce jako zbytek řádku
            process_function(name, params, body)

# Načtení souboru .pts
filename = "main.pts"
code = load_file(filename)

# Kompilace a vykonání načteného kódu
compile_and_execute(code, variables)

