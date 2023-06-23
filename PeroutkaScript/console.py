import os
os.system("echo off")
os.system("title Konzole jazyka PeroutkaScript")
os.system("color a")
os.system("cls")
print("----------------------------------------------")
print("To exit at any time type exit and press ENTER")
print("----------------------------------------------")
os.system("color 7")
print("")
print("")
def execute_peroutkascript(code):
    memory = []
    output = ""
    pointer = 0

    for char in code:
        if char == '+':
            memory.append(1)
        elif char == '-':
            memory.append(0)

    for value in memory:
        if value == 1:
            output += '1'
        else:
            output += '0'

    return output

# Nekonečná smyčka pro načítání a vykonávání kódu
while True:
    peroutkascript_code = input("Peroutkascript > ")

    if peroutkascript_code == "exit":
        break

    console_output = execute_peroutkascript(peroutkascript_code)
    print("Output:", console_output)
