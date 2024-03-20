# This is a sample Python script.
from Buissiness.service_biciclete import Service_Bicicleta
from Infrastructura.fisier_repo_biciclete import Fisier_Repo_Bicicleta
from Infrastructura.repository_bicicleta import Repo_Bicicleta
from UI.Consola import Consola
from Validare.Validator import Validator


# Press Shift+F10 to execute it or replace it with your code.
# Press Double Shift to search everywhere for classes, files, tool windows, actions, and settings.


def print_hi(name):
    # Use a breakpoint in the code line below to debug your script.
    print(f'Hi, {name}')  # Press Ctrl+F8 to toggle the breakpoint.


# Press the green button in the gutter to run the script.
if __name__ == '__main__':

    lista_bicilete=[]
    fis_repo=Fisier_Repo_Bicicleta(lista_bicilete)
    v=Validator()
    serv=Service_Bicicleta(fis_repo,v)
    ui=Consola(serv)
    ui.executa_comenzi()
