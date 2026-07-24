import { z } from 'zod';
import { TipoRecebivel } from './recebiveis';

export const recebivelFormSchema = z.object({
  cedente: z
    .string()
    .min(1, 'O nome do cedente é obrigatório')
    .max(100, 'O nome do cedente deve ter no máximo 100 caracteres'),
  
  valorOriginal: z
    .number({ error: 'Informe um valor numérico válido' })
    .positive('O valor original deve ser maior que zero'),
  
  vencimento: z
    .string()
    .min(1, 'A data de vencimento é obrigatória')
    .refine((data) => {
      const dataSelecionada = new Date(data + 'T00:00:00');
      const hoje = new Date();
      hoje.setHours(0, 0, 0, 0);
      return dataSelecionada >= hoje;
    }, 'A data de vencimento não pode ser retroativa'),
  
  tipo: z.nativeEnum(TipoRecebivel, { error: 'Selecione um tipo válido (DUPLICATA ou CHEQUE)' }),
  
  moedaOriginal: z
    .string()
    .min(3, 'A moeda deve ter 3 caracteres')
    .max(3, 'A moeda deve ter 3 caracteres')
    .transform((val) => val.toUpperCase()),
});

export type RecebivelFormValues = z.infer<typeof recebivelFormSchema>;
