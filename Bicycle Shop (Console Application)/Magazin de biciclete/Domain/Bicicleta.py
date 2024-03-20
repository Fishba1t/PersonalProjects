
class Bicicleta:

    def __init__(self,id,tip,pret):
        self.id = id
        self.tip = tip
        self.pret=pret

    def get_id(self):
        return self.id

    def get_tip(self):
        return self.tip

    def get_pret(self):
        return self.pret

    def __str__(self):
        return f'Bicileta=(ID={self.id},Tip={self.tip},Pret={self.pret})'